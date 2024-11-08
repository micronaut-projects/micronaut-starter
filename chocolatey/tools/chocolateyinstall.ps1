$version = '4.6.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '13DA1E299A32018973D8E176594A74EC88AE83A243012348E4CD27053161D53F'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
