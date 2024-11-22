$version = '4.7.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'BF9914E26A2E106B5DC519AC32A699749FD89E631A9DE0F4CE890755AA20594C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
