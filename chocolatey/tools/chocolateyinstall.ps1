$version = '4.0.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8BBA30C1F997C6403A0B0F770A4C30F5B3C83D389BAFE4028EA03F184367D684'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
