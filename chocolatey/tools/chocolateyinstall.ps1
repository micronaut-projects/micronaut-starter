$version = '2.5.12'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'CFF0ED4EA9AD621D9FCC48D53CD62636EA039C6AC6A644030350F033FEE180A5'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
