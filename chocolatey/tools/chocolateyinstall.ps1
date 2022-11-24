$version = '3.7.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '603854725C64B1726665C7A77278CA31C043E0B8F9F1AB0C3CEA5B9909068AEE'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
