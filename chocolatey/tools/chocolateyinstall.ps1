$version = '4.3.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'CF58DA99EFF8C0A7F57A6A785BA4EE86F953DB54CE9C4CAA1AE381E2A0405229'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
