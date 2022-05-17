$version = '3.4.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '839DF26C27165510DA699F626F93357A33D4B765A9A748214DB22C61CEEE11B9'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
